/**
 * Connects buttons to charts.
 * @param sectionId Section that contains graph.
 * @param graphId Graph which will have the buttons connected.
 */
function linkButtonsToChart(sectionId, graphId) {
    const chart = $(graphId).ejChart("instance");

    // no forecast
    $(`${sectionId} .forecast-panel .no-forecast`).on("click", () => {
        chart.model.series[0].trendlines[0].forwardForecast = 1;
        chart.redraw();
    });

    // one week forecast
    $(`${sectionId} .forecast-panel .week-forecast`).on("click", () => {
        chart.model.series[0].trendlines[0].forwardForecast = 7;
        chart.redraw();
    });

    // one month forecast
    $(`${sectionId} .forecast-panel .month-forecast`).on("click", () => {
        chart.model.series[0].trendlines[0].forwardForecast = 30;
        chart.redraw();
    });

    // three months forecast
    $(`${sectionId} .forecast-panel .three-months-forecast`).on("click", () => {
        chart.model.series[0].trendlines[0].forwardForecast = 90;
        chart.redraw();
    });
}

const confirmedColor = "#ff781e";
const deathsColor = "#E94649";
const activeCasesColor = "#735cff";
const recoveriesColor = "#41e540";

const graphs = [
    {type: "forecast", graphId: "#confirmed-graph", sectionId: "#confirmed-section", color: confirmedColor,
        apiData:"confirmed", label: "Liczba zarażeń"},
    {type: "forecast", graphId: "#deaths-graph", sectionId: "#deaths-section", color: deathsColor,
        apiData:"deaths", label: "Liczba zgonów"},
    {type: "forecast", graphId: "#active-cases-graph", sectionId: "#active-cases-section", color: activeCasesColor,
        apiData:"active_cases", label: "Liczba aktywnych przypadków"},
    {type: "bar", graphId: "#daily-deaths-graph", color: deathsColor, apiData:"new_deaths",
        label: "Dzienna liczba zgonów"},
    {type: "bar", graphId: "#daily-confirmed-graph", color: confirmedColor, apiData:"new_confirmed",
        label: "Dzienna liczba potwierdzonych przypadków"},
    {type: "bar", graphId: "#daily-recoveries-graph", color: recoveriesColor, apiData:"new_recoveries",
        label: "Dzienna liczba wyzdrowień"}
];

/**
 * Repaints graphs. Can be used e.g. after resizing window.
 */
function repaintGraphs() {
    graphs.forEach(graph => $(graph.graphId).ejChart("instance").redraw());
}

function createBasicForecastGraph(graph) {
    const {graphId, sectionId, apiData, color, label} = graph;

    // getting data from api
    axios.get(`/api/${apiData}`)
        .then(function (response) {
            // mapping data. Converting string to Date
            const data = response.data.map(el => {return {value: el.value, date: new Date(el.date)}});

            // creates graph
            $(graphId).ejChart({
                primaryXAxis: {
                    alignment: "center",
                    labelIntersectAction : 'hide',
                    labelFormat: 'dd/MM/yy',
                    labelRotation: 45,
                    maximumLabels: 2.5
                },
                series:[{
                    tooltip: {
                        visible: true
                    },
                    fill: color,
                    name: label,
                    trendlines: [{
                        visibility: "visible",
                        type: "polynomial",
                        forwardForecast: 7,
                        polynomialOrder: 6,
                        name: 'Prognoza',
                        fill: '#1a4fc0'
                    }],
                    type: "line",
                    width: 0,
                    enableAnimation: true,
                    marker: {
                        border: {width: 0},
                        shape: 'circle',
                        size: {
                            height: 10, width: 10
                        },
                        visible: true
                    },
                    dataSource: data,
                    xName: "date",
                    yName: "value"
                }]
            });

            // remove loading label
            document.querySelector(graphId).removeChild(document.querySelector(`${graphId} .loading`))

            // link forecast buttons to chart
            linkButtonsToChart(sectionId, graphId);
        })
        .catch(function (error) {
            console.log(error);
        });
}

function createBasicBarGraph(graph) {
    const {graphId, apiData, color, label} = graph;

    // getting data from api
    axios.get(`/api/${apiData}`)
        .then(function (response) {
            // mapping data. Converting string to Date
            const data = response.data.map(el => {return {value: el.value, date: new Date(el.date)}});

            // creates graph
            $(graphId).ejChart({
                primaryXAxis: {
                    alignment: "center",
                    labelIntersectAction : 'hide',
                    labelFormat: 'dd/MM/yy',
                    labelRotation: 45,
                    maximumLabels: 10
                },
                series:[{
                    fill: color,
                    highlightSettings: {
                        enable: true,
                        mode: 'point'
                    },
                    tooltip: {
                        visible: true
                    },
                    name: label,
                    enableAnimation: true,
                    dataSource: data,
                    xName: "date",
                    yName: "value"
                }]
            });

            // remove loading label
            document.querySelector(graphId).removeChild(document.querySelector(`${graphId} .loading`))
        })
        .catch(function (error) {
            console.log(error);
        });
}

/**
 * Creates graphs after loading page.
 */
$(window).on('load', function(event) {
    $(function () {
        graphs.forEach(graph => {
            switch (graph.type) {
                case "forecast":
                    createBasicForecastGraph(graph);
                    break;
                case "bar":
                    createBasicBarGraph(graph);
                    break;
            }
        });
    });
});

/**
 * Repaints graphs after resizing window.
 */
$(window).resize(function() {
    repaintGraphs();
});
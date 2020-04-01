// todo temporary data
let chartData = [
    {date:new Date(2020, 3,4), cases: 1},
    {date:new Date(2020, 3,5), cases: 1},
    {date:new Date(2020, 3,6), cases: 5},
    {date:new Date(2020, 3,7), cases: 6},
    {date:new Date(2020, 3,8), cases: 11},
    {date:new Date(2020, 3,9), cases: 17},
    {date:new Date(2020, 3,10), cases: 22},
    {date:new Date(2020, 3,11), cases: 31},
    {date:new Date(2020, 3,12), cases: 51},
    {date:new Date(2020, 3,13), cases: 68},
    {date:new Date(2020, 3,14), cases: 104},
    {date:new Date(2020, 3,15), cases: 125},
    {date:new Date(2020, 3,16), cases: 177},
    {date:new Date(2020, 3,17), cases: 238},
    {date:new Date(2020, 3,18), cases: 287},
    {date:new Date(2020, 3,19), cases: 355},
    {date:new Date(2020, 3,20), cases: 425},
    {date:new Date(2020, 3,21), cases: 536},
    {date:new Date(2020, 3,22), cases: 634},
];

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

function createConfirmedGraph() {
    const graphId = "#confirmed-graph";
    const sectionId = "#confirmed-section";
    axios.get('/api/confirmed')
        .then(function (response) {
            const data = response.data.map(el => {return {confirmed: el.confirmed, date: new Date(el.date)}});

            // create graph
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
                    name: 'Liczba zarażeń',
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
                        shape: 'circle',
                        size: {
                            height: 10, width: 10
                        },
                        visible: true
                    },
                    dataSource: data,
                    xName: "date",
                    yName: "confirmed"
                }]
            });

            // link forecast buttons to chart
            linkButtonsToChart(sectionId, graphId);
        })
        .catch(function (error) {
            console.log(error);
        });
}

/**
 * Template method that create example graph
 */
function createSecondExample() {
    const graphId = "#example-graph2";
    const sectionId = "#second-section";

    // create graph
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
            name: 'Liczba zarażeń',
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
                shape: 'circle',
                size: {
                    height: 10, width: 10
                },
                visible: true
            },
            dataSource: chartData,
            xName: "date",
            yName: "cases"
        }]
    });

    // link forecast buttons to chart
    linkButtonsToChart(sectionId, graphId);
}

/**
 * Repaints graphs. Can be used e.g. after resizing window.
 */
function repaintGraphs() {
    $("#confirmed-graph").ejChart("instance").redraw();
    $("#example-graph2").ejChart("instance").redraw();
}

/**
 * Creates graphs after loading page.
 */
$(window).on('load', function(event) {
    $(function () {
        createConfirmedGraph();
        createSecondExample();
    });
});

/**
 * Repaints graphs after resizing window.
 */
$(window).resize(function() {
    repaintGraphs();
});
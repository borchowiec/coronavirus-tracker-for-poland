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

function linkButtonsToChart(sectionId, graphId) {
    var chart = $(graphId).ejChart("instance");

    $(`${sectionId} .forecast-panel .no-forecast`).on("click", () => {
        chart.model.series[0].trendlines[0].forwardForecast = 1;
        chart.redraw();
    });

    $(`${sectionId} .forecast-panel .week-forecast`).on("click", () => {
        chart.model.series[0].trendlines[0].forwardForecast = 7;
        chart.redraw();
    });

    $(`${sectionId} .forecast-panel .month-forecast`).on("click", () => {
        chart.model.series[0].trendlines[0].forwardForecast = 30;
        chart.redraw();
    });

    $(`${sectionId} .forecast-panel .three-months-forecast`).on("click", () => {
        chart.model.series[0].trendlines[0].forwardForecast = 90;
        chart.redraw();
    });
}

function createFirstExample() {
    const graphId = "#example-graph1";
    const sectionId = "#first-section";

    // todo za dużo punktów na skali

    // create graph
    $(graphId).ejChart({
        primaryXAxis: {
            labelFormat: 'dd/MM/yy'
        },
        series:[{
            trendlines: [{
                visibility: "visible",
                type: "polynomial",
                forwardForecast: 7,
                polynomialOrder: 6
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

function createSecondExample() {
    $("#example-graph2").ejChart({
        series:[{
            trendlines: [{
                visibility: "visible",
                type: "polynomial"
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
}

function repaintGraphs() {
    $("#example-graph1").ejChart("instance").redraw();
    $("#example-graph2").ejChart("instance").redraw();
}

$(window).on('load', function(event) {
    $(function () {
        createFirstExample();
        createSecondExample();
    });
});

$(window).resize(function() {
    repaintGraphs();
});
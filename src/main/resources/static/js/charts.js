let chartData = [
    {date:"04.03.2020", cases: 1},
    {date:"05.03.2020", cases: 1},
    {date:"06.03.2020", cases: 5},
    {date:"07.03.2020", cases: 6},
    {date:"08.03.2020", cases: 11},
    {date:"09.03.2020", cases: 17},
    {date:"10.03.2020", cases: 22},
    {date:"11.03.2020", cases: 31},
    {date:"12.03.2020", cases: 51},
    {date:"13.03.2020", cases: 68},
    {date:"14.03.2020", cases: 104},
    {date:"15.03.2020", cases: 125},
    {date:"16.03.2020", cases: 177},
    {date:"17.03.2020", cases: 238},
    {date:"18.03.2020", cases: 287},
    {date:"19.03.2020", cases: 355},
    {date:"20.03.2020", cases: 425},
    {date:"21.03.2020", cases: 536},
    {date:"22.03.2020", cases: 634}
];

function repaintGraphs() {
    $("#example-graph1").ejChart({
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

$(window).on('load', function(event) {
    $(function () {
        repaintGraphs();
    });
});

$( window ).resize(function() {
    repaintGraphs();
});
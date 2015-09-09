package com.kaku.weac.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.util.HttpCallbackListener;
import com.kaku.weac.util.WeatherUtil;
import com.kaku.weac.view.LineChartView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 天气fragment
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class WeaFragment extends Fragment {
    /**
     * Log tag ：WeaFragment
     */
    private static final String LOG_TAG = "WeaFragment";
/*
    private LineData data;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;
    private Random random;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fm_wea, container, false);

        LineChartView chartDay = (LineChartView) view.findViewById(R.id.line_chart);
        chartDay.setTemp(new int[]{32, 31, 31, 30, 25, 26});
        chartDay.setTextSpace(10);
        int colorDay = getResources().getColor(R.color.yellow_hot);
        chartDay.setLineColor(colorDay);
        chartDay.setPointColor(colorDay);

        LineChartView chartNight = (LineChartView) view.findViewById(R.id.line_chart2);
        chartNight.setTemp(new int[]{20, 25, 24, 25, 20, 20});
        chartNight.setTextSpace(-10);
        int colorNight = getResources().getColor(R.color.blue_ice);
        chartNight.setLineColor(colorNight);
        chartNight.setPointColor(colorNight);

        /*
        LineChart chart = (LineChart) view.findViewById(R.id.chart);
        xVals=new ArrayList<>();
        yVals=new ArrayList<>();
        random=new Random();
        Entry e = new Entry(28, 0);

        yVals.add(new Entry(28, 0));
        yVals.add(new Entry(29, 1));
        yVals.add(new Entry(25, 2));
        yVals.add(new Entry(23, 3));
        yVals.add(new Entry(23, 4));
        yVals.add(new Entry(21, 5));
        for(int i=0;i<6;i++){
//            float profix=random.nextFloat();
//            yVals.add(new Entry(profix,i));
            xVals.add("");
        }
        dataSet=new LineDataSet(yVals,"");
        dataSet.setColor(getResources().getColor(R.color.red));
        dataSet.setLineWidth(2);
        dataSet.setCircleColor(Color.WHITE);
        dataSet.setCircleSize(5);

        data=new LineData(xVals,dataSet);
        data.setValueTextColor(getResources().getColor(R.color.white));
        data.setValueTextSize(14);
        data.setDrawValues(false);
        chart.setData(data);
        chart.setDescription("");
        chart.setDrawBorders(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false);
        chart.setScaleEnabled(false);*/
//        chart.setDrawMarkerViews(false);

//        chart.setGridBackgroundColor(Color.BLACK);
//        chart.animateY(3000);


/*
        LineChartView mLineChartView = (LineChartView) view.findViewById(R.id.chart);
        LineChartView mLineChartView2 = (LineChartView) view.findViewById(R.id.chart2);
//
//        chart.setInteractive(true);
////        chart.setZoomType(ZoomType zoomType);
////        chart.setContainerScrollEnabled(true, ContainerScrollType type);
//
////        ChartData.setAxisXBottom(Axis axisX);
////        ColumnChartData.setStacked(boolean isStacked);
////        Line.setStrokeWidth(int strokeWidthDp);
//
//        List<PointValue> values = new ArrayList<PointValue>();
//        values.add(new PointValue(0, 2));
//        values.add(new PointValue(1, 4));
//        values.add(new PointValue(2, 3));
//        values.add(new PointValue(3, 4));
//
//        //In most cased you can call data model methods in builder-pattern-like manner.
//        Line line = new Line(values).setColor(getResources().getColor(R.color.white)).setCubic(true);
//        List<Line> lines = new ArrayList<Line>();
//        lines.add(line);
//
//        LineChartData data = new LineChartData();
//        data.setLines(lines);
//
//
//        chart.setLineChartData(data);

        List<PointValue> mPointValues = new ArrayList<>();
        PointValue p0 = new PointValue(0, 22);
        p0.setLabel("22°");
        mPointValues.add(p0);
        PointValue p1 = new PointValue(1, 26);
        p1.setLabel("26°");
        mPointValues.add(p1);
        PointValue p2 = new PointValue(2, 28);
        p2.setLabel("28°");
        mPointValues.add(p2);
        PointValue p3 = new PointValue(3, 27);
        p3.setLabel("27°");
        mPointValues.add(p3);
        PointValue p4 = new PointValue(4, 26);
        p4.setLabel("26°");
        mPointValues.add(p4);
        PointValue p5 = new PointValue(5, 24);
        p5.setLabel("24°");
        mPointValues.add(p5);

        List<PointValue> mPointValues2 = new ArrayList<>();
        PointValue p20 = new PointValue(0, 18);
        p20.setLabel("18°");
        mPointValues2.add(p20);
        PointValue p21 = new PointValue(1, 18);
        p21.setLabel("18°");
        mPointValues2.add(p21);
        PointValue p22 = new PointValue(2, 19);
        p22.setLabel("19°");
        mPointValues2.add(p22);
        PointValue p23 = new PointValue(3, 20);
        p23.setLabel("20°");
        mPointValues2.add(p23);
        PointValue p24 = new PointValue(4, 20);
        p24.setLabel("20°");
        mPointValues2.add(p24);
        PointValue p25 = new PointValue(5, 17);
        p25.setLabel("17°");
        mPointValues2.add(p25);

//        List<AxisValue> mAxisValues = new ArrayList<>();
//        List<String> str = new ArrayList<>();
//        str.add(0,"31°");
//        str.add(1,"28°");
//        str.add(2,"23°");
//        str.add(3,"25°");
//        str.add(4,"26°");
//        str.add(5,"28°");
//
//        mAxisValues.add(new AxisValue(0).setLabel("31°"));
//        mAxisValues.add(new AxisValue(1).setLabel("28°"));
//        mAxisValues.add(new AxisValue(2).setLabel("23°"));
//        mAxisValues.add(new AxisValue(3).setLabel("25°"));
//        mAxisValues.add(new AxisValue(4).setLabel("26°"));

//        for (int i = 0; i < 6 ; i++) {
//            mPointValues.add(new PointValue(i, new Random().nextInt(10)));
////            mAxisValues.add(new AxisValue(i).setLabel("1")); //为每个对应的i设置相应的label(显示在X轴)
//        }
        Line line = new Line(mPointValues).setColor(getResources().getColor(R.color.blue_green))
                .setCubic(false);
        line.setHasLabels(true);
//        line.setFilled(false);
//        line.setPointRadius(5);
//        line.setStrokeWidth(1);
//        line.setHasPoints(true);
        line.setPointColor(getResources().getColor(R.color.white));
        List<Line> lines = new ArrayList<>();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
//        data.setValueLabelTextSize(14);
        data.setValueLabelsTextColor(getResources().getColor(R.color.white));
//        data.setValueLabelBackgroundAuto(false);
        data.setValueLabelBackgroundEnabled(false);


        Line line2 = new Line(mPointValues2).setColor(getResources().getColor(R.color.red))
                .setCubic(false);
        line2.setHasLabels(true);
        line.setFilled(false);
        line.setPointRadius(5);
        line.setStrokeWidth(3);
        line.setHasPoints(true);
        line2.setPointColor(getResources().getColor(R.color.white));
        List<Line> lines2 = new ArrayList<>();
        lines2.add(line2);
        LineChartData data2 = new LineChartData();
        data2.setLines(lines2);
        data2.setValueLabelsTextColor(getResources().getColor(R.color.white));
        data2.setValueLabelBackgroundEnabled(false);



//坐标轴
//        Axis axisX = new Axis(); //X轴
//        axisX.setHasTiltedLabels(true);
//        axisX.setTextColor(getResources().getColor(R.color.white));
//        axisX.setName("采集时间");
//        axisX.setMaxLabelChars(10);
//        axisX.setValues(mAxisValues);
//        data.setAxisXTop(axisX);

//        Axis axisY = new Axis();  //Y轴
//        axisY.setMaxLabelChars(7); //默认是3，只能看最后三个数字
//        data.setAxisYLeft(axisY);

//设置行为属性，支持缩放、滑动以及平移
//        mLineChartView.setInteractive(true);
//        mLineChartView.setZoomType(ZoomType.HORIZONTAL);
//        mLineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
//        mLineChartView.setVisibility(View.VISIBLE);
        mLineChartView.setLineChartData(data);
        mLineChartView2.setLineChartData(data2);*/

//
//        LineChartView chartView = (LineChartView) view.findViewById(R.id.linechart);
//        LineSet lineSet = new LineSet();
//        lineSet.addPoint(new Point("1", 20));
//        lineSet.addPoint(new Point("2", 30));
//        lineSet.addPoint(new Point("2", 50));
//        // Style dots
//        lineSet.setDotsColor(Color.WHITE);
//        lineSet.setDotsRadius(20);
//        lineSet.setDotsStrokeThickness(3);
//        lineSet.setDotsStrokeColor(Color.BLACK);
//        // Style line
////		lineSet.setLineThickness(5);
////		lineSet.setLineColor(Color.BLUE);
//        // Style background fill
////		lineSet.setFill(true);
////		lineSet.setFillColor(Color.BLUE);
//        // Style type
////		lineSet.setDashed(true);
//        lineSet.setSmooth(true);
//        chartView.addData(lineSet);

        Button readButton = (Button) view.findViewById(R.id.read_wea);
        final TextView tv = (TextView) view.findViewById(R.id.wea_prompt_tv);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherUtil.sendHttpRequest("http://wthrcdn.etouch.cn/WeatherApi?citykey=101030100",
                        new HttpCallbackListener() {
                            @Override
                            public void onFinish(final String response) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.setText(response);

                                        try {
                                            // 外部存储根路径
                                            String fileName = Environment.getExternalStorageDirectory()
                                                    .getAbsolutePath();
                                            // 录音文件路径
                                            fileName += "/WeaAlarmClock/wea2.txt";
                                            File file = new File(fileName);
                                            if (!file.exists()) {
                                                file.createNewFile();
                                            }

                                            FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                            BufferedWriter bw = new BufferedWriter(fw);
                                            bw.write(response);
                                            bw.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onError(final Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.setText("读取失败：" + e.toString());
                                    }
                                });
                            }
                        });
            }
        });

        return view;
    }

}

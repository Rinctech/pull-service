package org.example;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class MyProcessWindowFunction extends ProcessWindowFunction<MeterData, String , String, TimeWindow> {

    @Override
    public void process(String s, ProcessWindowFunction<MeterData, String, String, TimeWindow>.Context context, Iterable<MeterData> elements, Collector<String> out) throws Exception {
  System.out.println(s);
    }
}

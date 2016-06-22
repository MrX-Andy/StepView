package com.baoyachi.stepview.demo;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.baoyachi.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

/**
 * 日期：16/6/22 16:01
 * <p>
 * 描述：
 */
public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StepView setpview = (StepView) findViewById(R.id.step_flow);

        List<String> list6 = new ArrayList<>();
        list6.add("接单");
        list6.add("出发");
        list6.add("开始");
        list6.add("结束");
        list6.add("付账");
        list6.add("完成");
        setpview.setStepsViewIndicatorComplectingPosition(4)//设置完成的步数
                .setStepViewTexts(list6)//总步骤
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getBaseContext(), android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getBaseContext(), R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(getBaseContext(), android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getBaseContext(), R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.attention));//设置StepsViewIndicator AttentionIcon

    }

    void setData()
    {
        List<String> list1 = new ArrayList<>();
        list1.add("接单");
        List<String> list2 = new ArrayList<>();
        list2.add("接单");
        list2.add("出发");

        List<String> list3 = new ArrayList<>();
        list3.add("接单");
        list3.add("出发");
        list3.add("开始");

        List<String> list4 = new ArrayList<>();
        list4.add("接单");
        list4.add("出发");
        list4.add("开始");
        list4.add("结束");

        List<String> list5 = new ArrayList<>();
        list5.add("接单");
        list5.add("出发");
        list5.add("开始");
        list5.add("结束");
        list5.add("结账");
        List<String> list6 = new ArrayList<>();
        list6.add("接单");
        list6.add("出发");
        list6.add("开始");
        list6.add("结束");
        list6.add("结账");
        list6.add("完成");
    }
}
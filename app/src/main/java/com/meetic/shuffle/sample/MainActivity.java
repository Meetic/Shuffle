package com.meetic.shuffle.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.horizontal)
    public void launchHorizontal() {
        startActivity(new Intent(this, HorizontalActivity.class));
    }

    @OnClick(R.id.horizontalInline)
    public void launchHorizontalInline() {
        startActivity(new Intent(this, HorizontalInlineActivity.class));
    }

    @OnClick(R.id.horizontalInlineBehind)
    public void launchHorizontalInlineBehind() {
        startActivity(new Intent(this, HorizontalInlineBehindActivity.class));
    }

    @OnClick(R.id.horizontalInlineWithoutRotation)
    public void launchHorizontalWithoutRotation() {
        startActivity(new Intent(this, HorizontalInlineWithoutRotationActivity.class));
    }

    @OnClick(R.id.vertical)
    public void launchVertical() {
        startActivity(new Intent(this, VerticalActivity.class));
    }

    @OnClick(R.id.enableDisable)
    public void launchEnableDisable() {
        startActivity(new Intent(this, EnableDisableActivity.class));
    }

    @OnClick(R.id.stackChange)
    public void launchStackChange() {
        startActivity(new Intent(this, StackChangeActivity.class));
    }

    @OnClick(R.id.wrapContent)
    public void launchWrapContent() {
        startActivity(new Intent(this, HorizontalInlineWrapContentActivity.class));
    }
    @OnClick(R.id.restart)
    public void launchRestart() {
        startActivity(new Intent(this, RestartActivity.class));
    }

}

/*
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.duan.chao.DCZ_authenticator;

import android.os.Handler;
import android.util.Log;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Task that periodically notifies its listener about the time remaining until the value of a TOTP
 * counter changes.
 *
 * @author klyubin@google.com (Alex Klyubin)
 */
public class TotpCountdownTask implements Runnable {
  private final TotpCounter mCounter;
  private final TotpClock mClock;
  private final long mRemainingTimeNotificationPeriod;
  private final Handler mHandler = new Handler();

  public static long mLastSeenCounterValue = Long.MIN_VALUE;
  private boolean mShouldStop;
  private Listener mListener;

  /**
   * Listener notified of changes to the time remaining until the counter value changes.
   */
  public interface Listener {

    /**
     * Invoked when the time remaining till the TOTP counter changes its value.
     *
     * @param millisRemaining time (milliseconds) remaining.
     */
    void onTotpCountdown(long millisRemaining);

    /** Invoked when the TOTP counter changes its value. */
    void onTotpCounterValueChanged();
  }

  /**
   * Constructs a new {@code TotpRefreshTask}.
   *
   * @param counter TOTP counter this task monitors.
   * @param clock TOTP clock that drives this task.
   * @param remainingTimeNotificationPeriod approximate interval (milliseconds) at which this task
   *        notifies its listener about the time remaining until the @{code counter} changes its
   *        value.
   */
  public TotpCountdownTask(TotpCounter counter, TotpClock clock, long remainingTimeNotificationPeriod) {
    mCounter = counter;
    mClock = clock;
    mRemainingTimeNotificationPeriod = remainingTimeNotificationPeriod;
  }

  /**
   * Sets the listener that this task will periodically notify about the state of the TOTP counter.
   *
   * @param listener listener or {@code null} for no listener.
   */
  public void setListener(Listener listener) {
    mListener = listener;
  }

  /**
   * Starts this task and immediately notifies the listener that the counter value has changed.
   *
   * <p>The immediate notification during startup ensures that the listener does not miss any
   * updates.
   *
   * @throws IllegalStateException if the task has already been stopped.
   */
  public void startAndNotifyListener() {
    if (mShouldStop) {
      throw new IllegalStateException("Task already stopped and cannot be restarted.");
    }
    run();
  }

  /**
   * Stops this task. This task will never notify the listener after the task has been stopped.
   */
  public void stop() {
    mShouldStop = true;
  }

  @Override
  public void run() {
    if (mShouldStop) {
      return;
    }
    long now = mClock.currentTimeMillis();
    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format = new SimpleDateFormat("ss.SSS");
    String datetime = format.format(now);
    if(MyApplication.offset!=null&&MyApplication.offset!=0){
      now=now-MyApplication.offset;
      String datetime2 = format.format(now);
      if(Double.parseDouble(datetime2)>30){
        MainActivity.number=(long)((Double.parseDouble(datetime2)-30)*1000);
      }else {
        MainActivity.number=(long)((Double.parseDouble(datetime2))*1000);
      }
     // MainActivity.ceshi.setText(MainActivity.number+"");
    }else {
      if(Double.parseDouble(datetime)>30){
        MainActivity.number=(long)((Double.parseDouble(datetime)-30)*1000);
      }else {
        MainActivity.number=(long)((Double.parseDouble(datetime))*1000);
      }
     // MainActivity.ceshi.setText(MainActivity.number+"");
    }
    long counterValue = getCounterValue(now);
    if (mLastSeenCounterValue != counterValue) {
      mLastSeenCounterValue = counterValue;
      fireTotpCounterValueChanged();
    }
    fireTotpCountdown(getTimeTillNextCounterValue(now));

    scheduleNextInvocation();
  }

  private void scheduleNextInvocation() {
    long now = mClock.currentTimeMillis()-MyApplication.offset;
    long counterValueAge = getCounterValueAge(now);
    long timeTillNextInvocation =
        mRemainingTimeNotificationPeriod - (counterValueAge % mRemainingTimeNotificationPeriod);
    mHandler.postDelayed(this, timeTillNextInvocation);
  }

  private void fireTotpCountdown(long timeRemaining) {
    if ((mListener != null) && (!mShouldStop)) {
      mListener.onTotpCountdown(timeRemaining);
    }
  }

  private void fireTotpCounterValueChanged() {
    if ((mListener != null) && (!mShouldStop)) {
      mListener.onTotpCounterValueChanged();
    }
  }

  /**
   * Gets the value of the counter at the specified time instant.
   *
   * @param time time instant (milliseconds since epoch).
   */
  private long getCounterValue(long time) {
    return mCounter.getValueAtTime(Utilities.millisToSeconds(time));
  }

  /**
   * Gets the time remaining till the counter assumes its next value.
   *
   * @param time time instant (milliseconds since epoch) for which to perform the query.
   *
   * @return time (milliseconds) till next value.
   */
  private long getTimeTillNextCounterValue(long time) {
    long currentValue = getCounterValue(time);
    long nextValue = currentValue + 1;
    long nextValueStartTime = Utilities.secondsToMillis(mCounter.getValueStartTime(nextValue));
    return nextValueStartTime - time;
  }

  /**
   * Gets the age of the counter value at the specified time instant.
   *
   * @param time time instant (milliseconds since epoch).
   *
   * @return age (milliseconds).
   */
  private long getCounterValueAge(long time) {
    return time - Utilities.secondsToMillis(mCounter.getValueStartTime(getCounterValue(time)));
  }
}

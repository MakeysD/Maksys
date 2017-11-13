/*
 * Copyright 2012 Google Inc. All Rights Reserved.
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

package com.example.duan.chao.DCZ_authenticator.timesync;

import android.os.Bundle;

import com.example.duan.chao.DCZ_authenticator.wizard.WizardPageActivity;
import com.example.duan.chao.R;

import java.io.Serializable;

/**
 * Activity that displays more information about the Time Correction/Sync feature.
 *
 * @author klyubin@google.com (Alex Klyubin)
 */
public class AboutActivity extends WizardPageActivity<Serializable> {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setPageContentView(R.layout.timesync_about);
    setTextViewHtmlFromResource(R.id.details, R.string.timesync_about_feature_screen_details);

    setButtonBarModeMiddleButtonOnly();
    mMiddleButton.setText(R.string.ok);
  }

  @Override
  protected void onMiddleButtonPressed() {
    onBackPressed();
  }
}

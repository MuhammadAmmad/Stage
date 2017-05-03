/*
 * Copyright 2017 Hippo Seven
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

package com.hippo.stage.demo.scene;

/*
 * Created by Hippo on 5/3/2017.
 */

import static junit.framework.Assert.assertNotNull;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hippo.android.dialog.base.DialogView;
import com.hippo.stage.demo.App;
import com.hippo.stage.dialog.AlertDialogScene;

public abstract class DialogScene extends AlertDialogScene {

  @Nullable
  @Override
  protected final View onCreateContentView(@NonNull LayoutInflater inflater,
      @NonNull ViewGroup container) {
    DialogView view = onCreateDialogView(inflater, container);
    view.setDialog(this);
    return view;
  }

  /**
   * Create a {@link DialogView} for this dialog.
   */
  @NonNull
  protected abstract DialogView onCreateDialogView(@NonNull LayoutInflater inflater,
      @NonNull ViewGroup container);

  @Override
  protected void onDestroyView(@NonNull View view) {
    super.onDestroyView(view);
    App app = (App) getApplication();
    assertNotNull(app);
    app.getRefWatcher().watch(view);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    App app = (App) getApplication();
    assertNotNull(app);
    app.getRefWatcher().watch(this);
  }
}

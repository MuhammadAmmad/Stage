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

package com.hippo.stage.dialog;

/*
 * Created by Hippo on 5/3/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hippo.stage.Scene;

/**
 * {@code DialogScene} shows a view in dialog style.
 */
public class DialogScene extends Scene implements DialogInterface {

  public static final int DEFAULT_THEME = 0;

  private static final String KEY_THEME_ID = "DialogScene:theme_id";
  private static final String KEY_THEME_ATTR_ID = "DialogScene:theme_attr_id";
  private static final String KEY_CANCELLABLE = "DialogScene:cancellable";
  private static final String KEY_CANCELLED_ON_TOUCH_OUTSIDE =
      "DialogScene:cancelled_on_touch_outside";

  private int themeId = DEFAULT_THEME;
  private int themeAttrId = android.R.attr.dialogTheme;

  private boolean cancellable = true;
  private boolean cancelledOnTouchOutside = true;

  private boolean cancelled;

  @Override
  protected void onCreate(@Nullable Bundle args) {
    super.onCreate(args);
    setOpacity(TRANSPARENT);
  }

  /**
   * Sets theme id for this dialog.
   * {@link #DEFAULT_THEME} for the default theme.
   * <p>
   * The value supplied here will be retained across dialog scene destroy and
   * creation.
   */
  public void setThemeId(int themeId) {
    this.themeId = themeId;
  }

  /**
   * Sets theme attribute id for this dialog.
   * {@link #DEFAULT_THEME} for the default theme.
   * <p>
   * The value supplied here will be retained across dialog scene destroy and
   * creation.
   */
  public void setThemeAttrId(int themeAttrId) {
    this.themeAttrId = themeAttrId;
  }

  /**
   * Sets whether this dialog is cancellable with the
   * {@link android.view.KeyEvent#KEYCODE_BACK BACK} key.
   * <p>
   * The value supplied here will be retained across dialog scene destroy and
   * creation.
   */
  public void setCancellable(boolean flag) {
    cancellable = flag;
  }

  /**
   * Sets whether this dialog is cancelled when touched outside the window's
   * bounds. If setting to true, the dialog is set to be cancellable if not
   * already set.
   * <p>
   * The value supplied here will be retained across dialog scene destroy and
   * creation.
   *
   * @param cancel Whether the dialog should be cancelled when touched outside
   *               the window.
   */
  public void setCancelledOnTouchOutside(boolean cancel) {
    if (cancel && !cancellable) {
      cancellable = true;
    }

    if (cancelledOnTouchOutside != cancel) {
      cancelledOnTouchOutside = cancel;
      View view = getView();
      if (view != null) {
        DialogRootView root = (DialogRootView) view.findViewById(R.id.sd_dialog_root);
        if (root != null) {
          root.setCancelledOnTouchOutside(cancel);
        }
      }
    }
  }

  private int resolveThemeId() {
    if (themeId != DEFAULT_THEME) {
      return themeId;
    } else if (themeAttrId != DEFAULT_THEME) {
      TypedValue outValue = new TypedValue();
      getActivity().getTheme().resolveAttribute(themeAttrId, outValue, true);
      return outValue.resourceId;
    } else {
      return 0;
    }
  }

  // Apply theme to LayoutInflater
  private LayoutInflater resolveLayoutInflater(LayoutInflater inflater) {
    int resolvedThemeId = resolveThemeId();
    if (resolvedThemeId != 0) {
      Context contextThemeWrapper = new ContextThemeWrapper(inflater.getContext(), resolvedThemeId);
      inflater = inflater.cloneInContext(contextThemeWrapper);
    }
    return inflater;
  }

  @NonNull
  @Override
  protected final View onCreateView(@NonNull LayoutInflater inflater,
      @NonNull ViewGroup container) {
    inflater = resolveLayoutInflater(inflater);
    View view = inflater.inflate(R.layout.sd_scene_dialog, container, false);

    DialogRootView root = (DialogRootView) view.findViewById(R.id.sd_dialog_root);
    root.setDialog(this);
    root.setCancelledOnTouchOutside(cancelledOnTouchOutside);

    ViewGroup content = (ViewGroup) view.findViewById(R.id.sd_dialog_content);
    View dialogContent = onCreateContentView(inflater, content);
    if (dialogContent != null) {
      content.addView(dialogContent);
    }

    return view;
  }

  /**
   * Called when the dialog is ready to display its view. {@code null} could be returned.
   * The standard body for this method will be
   * {@code return inflater.inflate(R.layout.my_layout, container, false);}, plus any binding code.
   *
   * @param inflater The LayoutInflater that should be used to inflate views
   * @param container The parent view that this dialog's view will eventually be attached to.
   *                  This dialog's view should NOT be added in this method. It is simply passed in
   *                  so that valid LayoutParams can be used during inflation.
   */
  @Nullable
  protected View onCreateContentView(@NonNull LayoutInflater inflater,
      @NonNull ViewGroup container) {
    return null;
  }

  @Override
  public boolean handleBack() {
    boolean result = super.handleBack();
    result = !cancellable || result;
    if (!result) {
      // This dialog will be cancelled
      if (!cancelled && !isDestroyed()) {
        cancelled = true;
        onCancel();
      }
    }
    return result;
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(KEY_THEME_ID, themeId);
    outState.putInt(KEY_THEME_ATTR_ID, themeAttrId);
    outState.putBoolean(KEY_CANCELLABLE, cancellable);
    outState.putBoolean(KEY_CANCELLED_ON_TOUCH_OUTSIDE, cancelledOnTouchOutside);
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    setThemeId(savedInstanceState.getInt(KEY_THEME_ID, themeId));
    setThemeAttrId(savedInstanceState.getInt(KEY_THEME_ATTR_ID, themeAttrId));
    setCancellable(savedInstanceState.getBoolean(KEY_CANCELLABLE, cancellable));
    setCancelledOnTouchOutside(savedInstanceState.getBoolean(
        KEY_CANCELLED_ON_TOUCH_OUTSIDE, cancelledOnTouchOutside));
  }

  /**
   * Cancel the dialog. This is essentially the same as calling {@link #dismiss()}, but it will
   * also call {@link #onCancel()}.
   */
  @Override
  public void cancel() {
    if (!cancelled && !isDestroyed()) {
      cancelled = true;
      onCancel();
      pop();
    }
  }

  /**
   * Pops this dialog scene.
   */
  @Override
  public void dismiss() {
    pop();
  }

  /**
   * This method will be invoked when the dialog is cancelled.
   */
  public void onCancel() {}
}

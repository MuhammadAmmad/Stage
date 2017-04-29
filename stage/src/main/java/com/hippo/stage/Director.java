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

package com.hippo.stage;

/*
 * Created by Hippo on 4/22/2017.
 */

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

/**
 * A {@code Director} can direct multiple stage.
 */
public interface Director {

  /**
   * Directs a {@link ViewGroup} as a {@link Stage}.
   * <p>
   * Use different container view for each {@code Stage}.
   * Set different ID for each container view.
   */
  @NonNull
  Stage direct(@NonNull ViewGroup container);

  int requireSceneId();

  @Nullable
  Activity getActivity();
}

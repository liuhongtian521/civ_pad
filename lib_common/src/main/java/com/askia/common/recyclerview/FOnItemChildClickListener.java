/**
 * Copyright 2015 bingoogolapple
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.askia.common.recyclerview;

import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName FOnItemChildClickListener
 * @author  WangHuanyu
 * @todo AdapterView和RecyclerView的item中子控件点击事件监听器
 * @date 2016/9/13 16:17
 */
public interface FOnItemChildClickListener {
    void onItemChildClick(ViewGroup parent, View childView, int position);
}
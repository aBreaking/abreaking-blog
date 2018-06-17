/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.core.cache;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.plugin.ehcache.CacheKit;

public class ActionCacheManager {

	private static String USE_JCACHE = "_use_jcache__";
	private static String USE_JCACHE_KEY = "_use_jcache_key__";
	private static String USE_JCACHE_CONTENT_TYPE = "_use_jcache_content_type__";

	public static String CACHE_NAME = "action";

	public static void clearCache() {
		CacheKit.removeAll(CACHE_NAME);
	}

	public static String getCache(String key) {
		return CacheKit.get(CACHE_NAME, key);
	}

	public static void putCache(HttpServletRequest request, Object value) {
		CacheKit.put(CACHE_NAME, getCacheKey(request), value);
	}
	
	public static void enableCache(HttpServletRequest request) {
		request.setAttribute(USE_JCACHE, true);
	}

	public static boolean isEnableCache(HttpServletRequest request) {
		return (Boolean) (request.getAttribute(USE_JCACHE) == null ? false : request.getAttribute(USE_JCACHE));
	}

	public static void setCacheKey(HttpServletRequest request, String key) {
		request.setAttribute(USE_JCACHE_KEY, key);
	}

	public static String getCacheKey(HttpServletRequest request) {
		return (String) request.getAttribute(USE_JCACHE_KEY);
	}

	public static void setCacheContentType(HttpServletRequest request, String contentType) {
		request.setAttribute(USE_JCACHE_CONTENT_TYPE, contentType);
	}

	public static String getCacheContentType(HttpServletRequest request) {
		return (String) request.getAttribute(USE_JCACHE_CONTENT_TYPE);
	}
}

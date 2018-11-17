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
package com.abreaking.freemarker.tag;

import com.abreaking.core.render.freemarker.JTag;
import com.abreaking.model.Taxonomy;
import com.abreaking.model.query.TaxonomyQuery;

import java.math.BigInteger;
import java.util.*;

public class TaxonomysTag extends JTag {
	
	public static final String TAG_NAME = "jp.taxonomys";

	private List<Taxonomy> filterList;

	private static volatile List<Taxonomy> rootList = new ArrayList<Taxonomy>();



	public TaxonomysTag() {
	}

	public TaxonomysTag(List<Taxonomy> taxonomys) {
		filterList = taxonomys;
	}


    @Override
    public void onRender() {
	    if(!rootList.isEmpty()){
            setVariable("taxonomys", rootList);
            renderBody();
            return;
        }

        String module = "article";
        String type = "category";
        String orderby = "parent_id";
        List<Taxonomy> list = TaxonomyQuery.me().findListByModuleAndType(module, type, orderby, null, null);
        formList(list);
        setVariable("taxonomys", rootList);
        renderBody();
    }

	@Deprecated
	public void onRender_back() {

		int count = getParamToInt("count", 0);
		count = count <= 0 ? 10 : count;

		String module = getParam("module");
		String activeClass = getParam("activeClass", "active");
		String type = getParam("type");
		String orderby = getParam("orderBy");

		BigInteger parentId = getParamToBigInteger("parentId");

		List<Taxonomy> list = TaxonomyQuery.me().findListByModuleAndType(module, type, orderby, parentId, count);
		if (filterList != null && list != null && list.size() > 0) {
			for (Taxonomy taxonomy : list) {
				taxonomy.initFilterList(filterList, activeClass);
			}
		}

		setVariable("taxonomys", list);


		//firstParent
		renderBody();

	}

	private void formList(List<Taxonomy> list){
		Map<BigInteger,Taxonomy> map =new HashMap<BigInteger,Taxonomy>();
        LinkedList<Taxonomy> linkedList = new LinkedList<>();

        for(Taxonomy t: list){
            if(new BigInteger("0").equals(t.getParentId())){
				map.put(t.getId(),t);
				continue;
			}

			if(map.containsKey(t.getParentId())){
                map.get(t.getParentId()).getChildList().add(t);
            }else{
                for(BigInteger id : map.keySet()){
                    for(Taxonomy _t : map.get(id).getChildList()){
                        if(t.getParentId().equals(_t.getId())){
                            _t.getChildList().add(t);
                            continue;
                        }
                    }
                }
            }
		}

		list.clear();
        for(BigInteger id : map.keySet()){
		    list.add(map.get(id));
        }

        rootList = list;
	}

    //每有增删改等操作，就让分类列表置空
    public static void setTaxonomyRootListNull(){
        rootList.clear();
    }

	public static void main(String args[]){
        Taxonomy t11 = new Taxonomy();
        t11.set("id",new BigInteger("1")).setParentId(new BigInteger("0"));
        Taxonomy t12 = new Taxonomy();
        t12.set("id",new BigInteger("2")).setParentId(new BigInteger("0"));
        HashMap<BigInteger,Taxonomy> map = new HashMap<BigInteger,Taxonomy>();
        map.put(t11.getId(),t11);
        map.put(t12.getId(),t12);
        System.out.println(map);
        Taxonomy t21 = new Taxonomy();
        t21.set("id",new BigInteger("10")).setParentId(new BigInteger("1"));
        map.get(new BigInteger("1")).getChildList().add(t21);

        for (BigInteger b : map.keySet()){
            Taxonomy _t1 = map.get(b);
            System.out.println(_t1);
            if(_t1.getChildList()!=null){
                System.out.println(_t1.getChildList());
            }
        }

    }

}

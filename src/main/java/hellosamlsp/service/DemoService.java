package hellosamlsp.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import hellosamlsp.model.Demo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DemoService {

    private static List<Demo> list = new ArrayList<Demo>();
    private static AtomicInteger maxId = new AtomicInteger(0);
    static {
        for (int i = 0; i < 5; i++) {
            Integer id = maxId.incrementAndGet();
            Demo demo = new Demo();
            demo.setId(id);
            demo.setName("name_" + id);
            demo.setComment("comment_" + id);
            list.add(demo);
        }
    }

    public List<Demo> searchDemo(String simpleSearch) {
        if (StringUtils.isBlank(simpleSearch)) {
            return list;
        }
        List<Demo> result = new ArrayList<Demo>();
        for (Demo demo : list) {
            if (demo.getName().contains(simpleSearch)) {
                result.add(demo);
            }
        }
        return result;
    }

    public void addDemo(Demo demo) {
        Assert.notNull(demo, "demo can not be null!");
        Assert.hasText(demo.getName(), "name can not be blank!");
        demo.setId(maxId.incrementAndGet());
        list.add(demo);
    }

    public void updateDemo(Demo demo) {
        for (Iterator<Demo> iterator = list.iterator(); iterator.hasNext();) {
            Demo demoInList = iterator.next();
            if (demoInList.getId().equals(demo.getId())) {
                demoInList.setName(demo.getName());
                demoInList.setComment(demo.getComment());
                break;
            }
        }
    }

    public void deleteDemo(Integer id) {
        Assert.notNull(id, "id can not be null!");
        for (Iterator<Demo> iterator = list.iterator(); iterator.hasNext();) {
            Demo demo = iterator.next();
            if (id.equals(demo.getId())) {
                iterator.remove();
                break;
            }
        }
    }

}

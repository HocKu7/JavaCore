package test.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseEntityTest {
    private BaseEntity item;
    @Before
    public void init(){
        item= new BaseEntity();
    }
    @Test
    public void getId(){
        item.setId(10L);
        Long actual=item.getId();
        Assert.assertTrue(actual==10L);
    }
    @Test
    public void setId(){
        item.setId(13L);
        Long actual=item.getId();
        Assert.assertTrue(actual==13L);
    }
}

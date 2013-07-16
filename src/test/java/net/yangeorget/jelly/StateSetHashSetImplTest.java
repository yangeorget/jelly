package net.yangeorget.jelly;

import org.testng.annotations.Test;


/**
 * @author y.georget
 */
@Test(groups = "fast")
public class StateSetHashSetImplTest
        extends StateSetAbstractTest {
    @Override
    StateSet newStateSet() {
        return new StateSetHashSetImpl();
    }
}

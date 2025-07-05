package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    LixeiraTest.class,
    ObserverTest.class,
    StrategyTest.class,
    BuilderTest.class,
    SingletonTest.class
})
public class AllTests {
}
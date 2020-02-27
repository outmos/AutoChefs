
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import testCases.FirstSuite;
import testCases.SecondSuite;

@RunWith(Suite.class)
@SuiteClasses({FirstSuite.class, SecondSuite.class})
/**
 *  Classe qui lance tous les tests disponibles a la suite
 */
public class TopLevelSuite {}

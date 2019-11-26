package groupe1_brillu_damand_guillet_renault.tests;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

import com.google.inject.Inject;

import groupe1_brillu_damand_guillet_renault.compilateur.CompilateurR;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class CompilateurRTest {
	
	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	@Test
	public void testR() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"C:/CSVFile/iris.csv\" separator ;\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "recall\n"
				+ "");
		CompilateurR c = new CompilateurR(result);
		c.traitement(result);
	}
}

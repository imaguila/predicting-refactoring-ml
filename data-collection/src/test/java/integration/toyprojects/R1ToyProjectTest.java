package integration.toyprojects;

import integration.IntegrationBaseTest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import refactoringml.db.No;
import refactoringml.db.Yes;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class R1ToyProjectTest extends IntegrationBaseTest {

	@Override
	protected String getRepo() {
		return "repos/r1";
	}

	@Test
	public void yes() {

		List<Yes> yesList = session.createQuery("From Yes where project = :project order by refactoringDate desc")
				.setParameter("project", project)
				.list();
		Assert.assertEquals(3, yesList.size());

		assertRefactoring(yesList, "e8895b22847c7c54a9e187f9f674db274e6bc103", "Extract Method", 1);
	}

	@Test
	public void no() {
		// there are no instances of no variables, as the repo is too small
		List<No> noList = session.createQuery("From No where project = :project")
				.setParameter("project", project)
				.list();
		Assert.assertEquals(0, noList.size());
	}

	@Test
	public void metrics() {
		// the next two assertions come directly from a 'cloc .' in the project
		Assert.assertEquals(9, project.getJavaLoc());

		Assert.assertEquals(1, project.getNumberOfProductionFiles() + project.getNumberOfTestFiles());

		Assert.assertEquals(1, project.getNumberOfProductionFiles());

		Assert.assertEquals(0, project.getNumberOfTestFiles());

		Assert.assertEquals(9, project.getProductionLoc());

	}

	@Test
	public void processMetricsAvailable() {
		List<Yes> yesList = session.createQuery("From Yes where project = :project order by refactoringDate desc")
				.setParameter("project", project)
				.list();

		for (Yes element : yesList) {
			Assert.assertFalse(element.getProcessMetrics().hasProcessMetrics());
		}
	}

}

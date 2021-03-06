package refactoringml.db;

import javax.persistence.*;

@Entity
@Table(name = "RefactoringCommit", indexes = {@Index(columnList = "project_id"), @Index(columnList = "level"), @Index(columnList = "refactoring"), @Index(columnList = "isTest")})
public class RefactoringCommit extends Instance {
	//Describe the refactoring e.g. "Rename Class" or "Extract Method"
	private String refactoring;
	//Describe the content of the refactoring e.g. "Rename Pets to Cat"
	@Lob
	private String refactoringSummary;

	@Deprecated // hibernate purposes
	public RefactoringCommit() {}

	public RefactoringCommit(Project project, CommitMetaData commitMetaData, String filePath, String className,
			   String refactoring, int refactoringLevel, String refactoringSummary,
			   ClassMetric classMetrics, MethodMetric methodMetrics, VariableMetric variableMetrics, FieldMetric fieldMetrics) {
		super(project, commitMetaData, filePath, className, classMetrics, methodMetrics, variableMetrics, fieldMetrics, refactoringLevel);
		this.refactoring = refactoring;
		this.refactoringSummary = refactoringSummary.trim();
	}

	public String getRefactoring() { return refactoring; }

	public String getRefactoringSummary (){return refactoringSummary;}

	@Override
	public String toString() {
		return "RefactoringCommit{" +
				"id=" + id +
				super.toString() +
				", refactoring='" + refactoring + '\'' +
				", refactoringSummary=" + refactoringSummary +
				'}';
	}
}
package refactoringml.util;

import org.apache.log4j.Logger;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class JGitUtils {

	private static final Logger log = Logger.getLogger(JGitUtils.class);

	public static String readFileFromGit (Repository repo, RevCommit commit, String filepath) throws IOException {
		log.debug("reading file " + filepath + " in commit " + commit.getName());

		try (TreeWalk walk = TreeWalk.forPath(repo, filepath, commit.getTree())) {
			if (walk != null) {
				byte[] bytes = repo.open(walk.getObjectId(0)).getBytes();
				return new String(bytes, StandardCharsets.UTF_8);
			} else {
				throw new IllegalArgumentException("No path found in " + commit.getName() + ": " + filepath);
			}
		}
	}

	public static String readFileFromGit (Repository repo, String commit, String filepath) throws IOException {

		ObjectId commitId = ObjectId.fromString(commit);
		RevWalk revWalk = new RevWalk(repo);
		RevCommit revCommit = revWalk.parseCommit( commitId );

		return readFileFromGit(repo, revCommit, filepath);
	}

	public static String extractProjectNameFromGitUrl(String gitUrl) {
		String[] splittedGitUrl = gitUrl.split("/");
		return splittedGitUrl[splittedGitUrl.length - 1].replace(".git", "");
	}


	public static Calendar getGregorianCalendar(RevCommit commit) {
		GregorianCalendar commitTime = new GregorianCalendar();
		commitTime.setTime(commit.getAuthorIdent().getWhen());
		commitTime.setTimeZone(commit.getAuthorIdent().getTimeZone());
		return commitTime;
	}

	public static RevWalk getReverseWalk(Repository repo, String mainBranch) throws IOException {
		RevWalk walk = new RevWalk(repo);
		walk.markStart(walk.parseCommit(repo.resolve(mainBranch)));
		walk.sort(RevSort.REVERSE);
		return walk;
	}

	//Generate the commit url with repository url and the commit ID
	//Local repositories without remote are formatted as: @local/repository/commit Id
	//TODO: evaluate if this pattern works for other repo hoster as well, e.g. bitbucket
	public static String generateCommitUrl(String repositoryUrl, String commitId, boolean isLocal){
		if (isLocal){
			return String.format("@local/%s/%s", repositoryUrl, commitId);
		}
		String cleanRepositoryUrl = repositoryUrl.replace(".git", "");
		return String.format("%s/commit/%s", cleanRepositoryUrl, commitId);
	}
}

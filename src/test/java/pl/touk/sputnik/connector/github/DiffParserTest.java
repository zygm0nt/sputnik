package pl.touk.sputnik.connector.github;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

public class DiffParserTest {

    @Test
    public void shouldParseGitDiff() throws Exception {
        // given
        // file to parse
        String input = Resources.toString(Resources.getResource("github.diff"), Charsets.UTF_8);

        // when
        // parse file
        Map<String, Map<Integer, Pair<Integer, String>>> parsedDiff = new DiffParser().parse(input);

        // then
        // have a map of - file, list of tuples (line changed, commit id)
        assertThat(parsedDiff.get("build.gradle")).hasSize(37);
        assertThat(parsedDiff.get("build.gradle"))
                .contains(entry(69, Pair.of(22, "a")),
                        entry(70, Pair.of(23, "a")),
                        entry(7, Pair.of(4, "a")),
                        entry(71, Pair.of(24, "a")),
                        entry(8, Pair.of(5, "a")),
                        entry(72, Pair.of(25,"a")));
    }

    @Test
    public void shouldAddComments() {
        // Map<String, (line changed, commitId)> x

        /*
            for each file in report
                for each line with warningns in file
                    position, sha = x get (file) get(line)
                    post a comment to github (position, sha, file, comment)

        */

    }

    @Test
    public void jgitTest() throws Exception {
        File gitWorkDir = new File("/home/marcin/projects/sputnik");
        Git git = Git.open(gitWorkDir);

        String oldHash = "e26a7d333428bd3f7017afbff0208027fdd6ddf4";

        ObjectId headId = git.getRepository().resolve("HEAD^{tree}");
        ObjectId oldId = git.getRepository().resolve(oldHash + "^{tree}");

        ObjectReader reader = git.getRepository().newObjectReader();

        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        oldTreeIter.reset(reader, oldId);
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(reader, headId);

        List<DiffEntry> diffs= git.diff()
                .setNewTree(newTreeIter)
                .setOldTree(oldTreeIter)
                .call();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter df = new DiffFormatter(out);
        df.setRepository(git.getRepository());

        for(DiffEntry diff : diffs)
        {
            df.format(diff);
            diff.getOldId();
            String diffText = out.toString("UTF-8");
            System.out.println(diffText);
            out.reset();
        }
    }
}

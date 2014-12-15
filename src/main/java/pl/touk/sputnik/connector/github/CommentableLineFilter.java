package pl.touk.sputnik.connector.github;

import com.beust.jcommander.internal.Maps;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.api.errors.GitAPIException;
import pl.touk.sputnik.review.ReviewFile;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class CommentableLineFilter {
    public Map<Integer, Pair<Integer, String>> commentableLines(final List<String> commitedShas, ReviewFile reviewFile, String refId) {
        String filename = reviewFile.getReviewFilename();
        Map<Integer, Pair<Integer, String>> result = Maps.newHashMap();

        try {
            Map<String, List<Integer>> blameMap = new GitBlameCommand().gitBlame(filename, refId);
            for (String sha : Iterables.filter(blameMap.keySet(), new Predicate<String>() {

                @Override
                public boolean apply(@Nullable String sha) {
                    return commitedShas.contains(sha);
                }
            })) {
                // fetch diff for an sha, and parse it FIXME
                for (Integer lineNo : blameMap.get(sha)) {
                    result.put(lineNo, Pair.of(0, "")); // FIXME
                }
            }
        } catch (IOException | GitAPIException e) {
            log.error("Error doing git-blame for file {} and sha {}", filename, refId);
        }

        return result;
    }
}

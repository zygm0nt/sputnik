package pl.touk.sputnik.connector.github;

import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.connector.ConnectorFacade;
import pl.touk.sputnik.connector.Connectors;
import pl.touk.sputnik.review.Review;
import pl.touk.sputnik.review.ReviewFile;

import java.util.List;

public class GithubFacade implements ConnectorFacade {

    private final GithubConnector githubConnector;

    public GithubFacade(@NotNull GithubConnector stashConnector) {
        this.githubConnector = stashConnector;
    }

    @Override
    public Connectors name() {
        return Connectors.GITHUB;
    }

    @NotNull
    @Override
    public List<ReviewFile> listFiles() {
        return null;
    }

    @Override
    public void setReview(@NotNull Review review) {

    }
}

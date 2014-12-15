package pl.touk.sputnik.connector.github;

enum ParserState {

    INITIAL,
    HEADER,
    FROM_FILE,
    TO_FILE,
    HUNK_START,
    FROM_LINE,
    TO_LINE,
    NEUTRAL_LINE,
    END

}
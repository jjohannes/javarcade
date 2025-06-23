package de.javarca.jamcatch.actors;

import de.javarca.model.Actor;
import de.javarca.model.ActorProperty;
import de.javarca.model.ActorPropertyModifier;
import de.javarca.model.ActorSet;
import de.javarca.jamcatch.actors.collisions.Collisions;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public class JamCatchActorSet implements ActorSet {
    private static final Logger LOG = LoggerFactory.getLogger(JamCatchActorSet.class);

    private final Set<Actor> actors;

    public JamCatchActorSet() {
        var itemsCsv = requireNonNull(JamCatchActorSet.class.getResourceAsStream("res/jamcatch.csv"));
        LOG.debug("Parsing 'jamcatch.csv'");
        actors = parse(itemsCsv).stream()
                .map(record -> new Actor(
                        record.get("SYMBOL").charAt(0),
                        Arrays.stream(ActorProperty.values())
                                .map(property -> parsePropertyValue(record, property))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet()),
                        Collisions.ALL.getOrDefault(record.get("SYMBOL").charAt(0), Map.of())
                )).collect(Collectors.toSet());
    }

    private static ActorPropertyModifier parsePropertyValue(CSVRecord record, ActorProperty property) {
        String value = record.get(property);
        if (!value.isBlank()) {
            return new ActorPropertyModifier(property, Integer.parseInt(value));
        }
        return null;
    }

    private static List<CSVRecord> parse(InputStream itemsCsv) {
        try {
            var format = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get();
            return CSVParser.parse(itemsCsv, UTF_8, format).getRecords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Actor> items() {
        return actors;
    }
}

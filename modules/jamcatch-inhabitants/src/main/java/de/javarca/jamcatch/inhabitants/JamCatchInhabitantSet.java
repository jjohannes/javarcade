package de.javarca.jamcatch.inhabitants;

import de.javarca.model.Inhabitant;
import de.javarca.model.InhabitantProperty;
import de.javarca.model.InhabitantPropertyModifier;
import de.javarca.model.InhabitantSet;
import de.javarca.jamcatch.inhabitants.collisions.Collisions;
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

public class JamCatchInhabitantSet implements InhabitantSet {
    private static final Logger LOG = LoggerFactory.getLogger(JamCatchInhabitantSet.class);

    private final Set<Inhabitant> inhabitants;

    public JamCatchInhabitantSet() {
        var itemsCsv = requireNonNull(JamCatchInhabitantSet.class.getResourceAsStream("res/jamcatch.csv"));
        LOG.debug("Parsing 'jamcatch.csv'");
        inhabitants = parse(itemsCsv).stream()
                .map(record -> new Inhabitant(
                        record.get("SYMBOL").charAt(0),
                        Arrays.stream(InhabitantProperty.values())
                                .map(property -> parsePropertyValue(record, property))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet()),
                        Collisions.ALL.getOrDefault(record.get("SYMBOL").charAt(0), Map.of())
                )).collect(Collectors.toSet());
    }

    private static InhabitantPropertyModifier parsePropertyValue(CSVRecord record, InhabitantProperty property) {
        String value = record.get(property);
        if (!value.isBlank()) {
            return new InhabitantPropertyModifier(property, Integer.parseInt(value));
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
    public Set<Inhabitant> items() {
        return inhabitants;
    }
}

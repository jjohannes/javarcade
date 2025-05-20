package app.javarcade.classic.items;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

import app.javarcade.base.model.Item;
import app.javarcade.base.model.ItemSet;
import app.javarcade.base.model.PlayerProperty;
import app.javarcade.base.model.PlayerPropertyModifier;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassicItemSet implements ItemSet {
    private static final Logger LOG = LoggerFactory.getLogger(ClassicItemSet.class);

    private final Set<Item> items;

    public ClassicItemSet() {
        var itemsCsv = requireNonNull(ClassicItemSet.class.getResourceAsStream("res/items.csv"));
        LOG.debug("Parsing 'items.csv'");
        items = parse(itemsCsv).stream()
                .map(record -> new Item(
                        record.get("SYMBOL").charAt(0),
                        Arrays.stream(PlayerProperty.values())
                                .map(property -> parsePropertyValue(record, property))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())))
                .collect(Collectors.toSet());
    }

    private static PlayerPropertyModifier parsePropertyValue(CSVRecord record, PlayerProperty property) {
        String value = record.get(property);
        if (!value.isBlank()) {
            return new PlayerPropertyModifier(property, Integer.parseInt(value));
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
    public Set<Item> items() {
        return items;
    }
}

package app.javarcade.classic.items.test;

import static org.assertj.core.api.Assertions.assertThat;

import app.javarcade.base.model.Item;
import app.javarcade.base.model.PlayerProperty;
import app.javarcade.base.model.PlayerPropertyModifier;
import app.javarcade.classic.items.ClassicItemSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CSVParserTest {

  @Test
  void asset_information_is_parsed_from_csv_file() {
    var itemSet = new ClassicItemSet();

    assertThat(itemSet.items())
        .contains(new Item('➟', Set.of(new PlayerPropertyModifier(PlayerProperty.SPEED, 1))));
  }
}

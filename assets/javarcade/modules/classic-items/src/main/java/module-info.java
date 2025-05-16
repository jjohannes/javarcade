module app.javarcade.classic.items {
    requires transitive app.javarcade.base.model;
    requires org.apache.commons.csv;
    requires org.slf4j;

    exports app.javarcade.classic.items;

    provides app.javarcade.base.model.ItemSet with
            app.javarcade.classic.items.ClassicItemSet;
}

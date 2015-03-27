# Tutorial available
Tutorial is available under the  [EMF Forms Page] (http://eclipsesource.com/blogs/tutorials/emf-forms-renderer/#vaadin) 


### Theme
The provided [CSS] (https://github.com/SirWayne/ecp-vaadin/blob/master/bundles/org.eclipse.emf.ecp.view.core.vaadin/APP/PUBLISHED/ecp_vaadin_default.css) is optimized for the Vaadin Valo Theme. If you want to use it with other themes, you need to adapt the styling.
Thanks [Marita Klein] (https://github.com/riddy) 

######  Example for reindeer:
```css
  .table-edit.table-edit.v-button,
  .table-remove.table-remove.v-button,
  .table-add.table-add.v-button {
    min-width: 16px;
    width: 24px;
    height: 16px;
    text-align: right;
  }
  .table-edit.table-edit.v-button .v-button-wrap,
  .table-remove.table-remove.v-button .v-button-wrap,
  .table-add.table-add.v-button .v-button-wrap {
    padding: 7px 1px;
    width: 8px;
    height: 8px;
  }
  .collapsible-panel-expand.collapsible-panel-expand.v-nativebutton-collapsible-panel-expand, 
  .collapsible-panel-collapsed.collapsible-panel-collapsed.v-nativebutton-collapsible-panel-collapsed {
    height: 24px;
    text-align: left !important;
  }
```

### New Feature

Now Tables and Lists can be ordered, if the EStructualFeature ordered attribute is true.
Thanks [Matthias Juchmes] (https://github.com/NeoDobby) 

![Ordered Table](http://sirwayne.github.io/ordererTable.png)
![Ordered List](http://sirwayne.github.io/orderedList.png)



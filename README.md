# Tutorial available
Tutorial is available under the  [EMF Forms Page] (http://eclipsesource.com/blogs/tutorials/emf-forms-renderer/#vaadin) 


### Theme
The provided [CSS] (https://github.com/SirWayne/ecp-vaadin/blob/master/bundles/org.eclipse.emf.ecp.view.core.vaadin/APP/PUBLISHED/ecp_vaadin_default.css) is optimized for the Vaadin Valo Theme. If you want to use it with other themes, you need to adapt the styling.
Thanks [riddy] (https://github.com/riddy) 

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
### Mandatory marker:
The Vaadin Framework does not allow one to define a custom mandatory marker. Therefore we decided to stay with the Vaadin mandatory marker but provide you an example on how to change the marker if necessary (```VTMandatoryStyleProperty.getMandatoryMarker``` is not interpreted). You can archieve this simply with some css code:

```css
    .v-required-field-indicator {
      visibility: hidden;
    }
    .v-required-field-indicator:before {
      visibility: visible;
      content: "°";
      color: blue;
    }
```

### New Feature :: Ordered Lists & Tables

Now Tables and Lists can be ordered, if the EStructualFeature *ordered* attribute is true.
Thanks [Matthias Juchmes] (https://github.com/NeoDobby) 

###### Table
![Ordered Table](http://sirwayne.github.io/table.png)

###### List
![Ordered List](http://sirwayne.github.io/list.png)


### New Feature :: Editing mode for Lists & Tables

Now Tables and Lists can be edited if the component is not readonly. 
A click on the edit-button will switch the corresponding line into the edit mode (see [Book of Vaadin] (https://vaadin.com/book/vaadin7/-/page/components.table.html#components.table.editing)). After the user has made his changes he can click outside the editable row or submit his changes via *enter* to apply them.

###### List
![Edit Button in List](http://sirwayne.github.io/list_edit.png)

###### Table
![Edit Mode in Table](http://sirwayne.github.io/table_edit.png)

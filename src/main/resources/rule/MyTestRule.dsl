[condition][CheckItem]There is a checkitem=item : CheckItem()
[condition][CheckItem]-The checkitem is need data=eval(item.isNeedData())
[condition][CheckItem]-The checkitem has no data=eval(item.hasData()==false)
[consequence][]Add {note} to the item=item.setNote({note}+item.getNote());
[consequence][]Print checkitem=System.out.println("print:"+item);

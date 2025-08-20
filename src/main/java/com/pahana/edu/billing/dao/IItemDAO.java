package com.pahana.edu.billing.dao;

import com.pahana.edu.billing.model.Item;
import java.util.List;

public interface IItemDAO {
    int addItem(Item item);
    Item getItemById(int id);
    List<Item> getAllItems();
    boolean updateItem(Item item);
    boolean deleteItem(int id);
}

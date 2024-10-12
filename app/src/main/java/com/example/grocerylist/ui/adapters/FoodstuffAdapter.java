package com.example.grocerylist.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grocerylist.R;
import com.example.grocerylist.data.models.Foodstuff;

import java.util.ArrayList;

public class FoodstuffAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Foodstuff> groceryListItems;
    private final Button finishButton;
    private boolean areButtonsVisible = true;

    public FoodstuffAdapter(Context context, ArrayList<Foodstuff> groceryListItems, Button finishButton) {
        this.context = context;
        this.groceryListItems = groceryListItems;
        this.finishButton = finishButton;
    }

    @Override
    public int getCount() {
        return groceryListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return groceryListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.itemName);
        ImageView copyButton = convertView.findViewById(R.id.copyButton);
        ImageView deleteButton = convertView.findViewById(R.id.deleteButton);

        Foodstuff foodstuff = (Foodstuff) getItem(position);

        itemName.setText(foodstuff.Name + ", " + foodstuff.Quantity + " piece/s, " + foodstuff.PricePerPiece + " RSD per piece");

        if (areButtonsVisible) {
            copyButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            copyButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }

        copyButton.setOnClickListener(v -> {
            Foodstuff copiedItem = new Foodstuff(foodstuff.Name, foodstuff.Quantity, foodstuff.PricePerPiece);
            groceryListItems.add(copiedItem);
            notifyDataSetChanged();
            updateFinishButtonState();
        });

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Grocery List Item Deletion");
            builder.setMessage("Are you sure you want to remove the selected item from the list?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    groceryListItems.remove(position);
                    notifyDataSetChanged();
                    updateFinishButtonState();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        });


        return convertView;
    }

    public void setCopyAndDeleteButtonsVisible(boolean visible) {
        this.areButtonsVisible = visible;
        notifyDataSetChanged();
    }

    private void updateFinishButtonState() {
        finishButton.setEnabled(!groceryListItems.isEmpty());
    }
}
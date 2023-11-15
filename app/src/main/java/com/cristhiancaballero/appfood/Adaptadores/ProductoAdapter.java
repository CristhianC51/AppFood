package com.cristhiancaballero.appfood.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cristhiancaballero.appfood.Modelos.Producto;
import com.cristhiancaballero.appfood.R;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private List<Producto> datos;

    public ProductoAdapter(List<Producto> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto item = datos.get(position);
        holder.productName.setText(item.getNombre_producto());
        holder.productPrice.setText(item.getPrecio());
        // Cargar la imagen usando Glide o Picasso desde la URL
        Glide.with(holder.productImage.getContext()).load(item.getFoto()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;
        Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

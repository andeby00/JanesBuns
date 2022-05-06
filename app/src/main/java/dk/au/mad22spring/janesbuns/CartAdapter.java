package dk.au.mad22spring.janesbuns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import dk.au.mad22spring.janesbuns.models.CreamBun;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CreamBunViewHolder> {
    public interface ICreamBunItemClickedListener {
        void onCreamBunClicked(int index);
    }

    StorageReference storageRef;
    private ICreamBunItemClickedListener listener;
    private List<CreamBun> creamBunList;

    public CartAdapter(ICreamBunItemClickedListener listener) {
        this.listener = listener;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void updateCreamBunList(List<CreamBun> list) {
        creamBunList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CreamBunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CreamBunViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CreamBunViewHolder holder, int position) {
        holder.txtName.setText(creamBunList.get(position).name);
        holder.txtPrice.setText(creamBunList.get(position).price.toString());
        holder.txtAmount.setText(creamBunList.get(position).amount.toString());
        holder.imgImage.setImageResource(R.drawable.cross);
//        storageRef.child(creamBunList.get(position).uri).getDownloadUrl().addOnCompleteListener(task -> {
//            Glide.with(holder.imgImage.getContext()).load(task.getResult()).placeholder(R.drawable.plus_sign).into(holder.imgImage);
//        });
    }

    @Override
    public int getItemCount() {
        return creamBunList.size();
    }

    class CreamBunViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgImage;
        TextView txtName, txtPrice, txtAmount;
        ICreamBunItemClickedListener listener;

        public CreamBunViewHolder(@NonNull View itemView, ICreamBunItemClickedListener creamBunItemClickedListener) {
            super(itemView);

            imgImage = itemView.findViewById(R.id.imgCartItemX);
            txtName = itemView.findViewById(R.id.txtCartItemName);
            txtAmount = itemView.findViewById(R.id.txtCartItemAmount);
            txtPrice = itemView.findViewById(R.id.txtCartItemPrice);
            listener = creamBunItemClickedListener;

            imgImage.setOnClickListener(view -> onClick(itemView));
        }

        public void onClick(View view) {
            listener.onCreamBunClicked(getBindingAdapterPosition());
        }
    }
}

package net.uoit.sofe4640.group4.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.uoit.sofe4640.group4.project.database.AppDatabaseHelper;
import net.uoit.sofe4640.group4.project.database.Scooter;

import java.util.List;

public class ScooterAdapter extends RecyclerView.Adapter<ScooterAdapter.ScooterViewHolder> {
    private List<Scooter> heldScooters;

    private OnScooterSelectedListener selectedListener;

    public static class ScooterViewHolder extends RecyclerView.ViewHolder {
        public final View parentView;
        public final TextView textView_coordinates;

        public ScooterViewHolder(View view) {
            super(view);

            parentView = view;
            textView_coordinates = view.findViewById(R.id.textView_content);
        }
    }

    public interface OnScooterSelectedListener {
        void onScooterSelect(int id);
    }

    public ScooterAdapter(List<Scooter> initialScooters, OnScooterSelectedListener listener) {
        super();

        heldScooters = initialScooters;
        selectedListener = listener;
    }

    @NonNull
    @Override
    public ScooterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_scooter, parent, false);
        return new ScooterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScooterViewHolder holder, int position) {
        Scooter scooter = heldScooters.get(position);

        StringBuilder builder = new StringBuilder();
        builder.append(scooter.address);

        if (scooter.distance > 0.0d) {
            builder.append(" (");
            builder.append((int)scooter.distance);
            builder.append("m away)");
        }

        holder.textView_coordinates.setText(builder.toString());

        // Register a click listener for the view.
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We refetch the Scooter here based on getAdapterPosition()'s value, since we can't rely on position being correct in this listener.
                selectedListener.onScooterSelect(heldScooters.get(holder.getAdapterPosition()).id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return heldScooters.size();
    }

    public void setNewScooterList(List<Scooter> scooters) {
        heldScooters = scooters;

        notifyDataSetChanged();
    }
}

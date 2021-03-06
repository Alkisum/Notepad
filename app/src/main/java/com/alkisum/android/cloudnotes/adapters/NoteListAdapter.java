package com.alkisum.android.cloudnotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alkisum.android.cloudnotes.R;
import com.alkisum.android.cloudnotes.model.Note;
import com.alkisum.android.cloudnotes.ui.ColorPref;
import com.alkisum.android.cloudnotes.ui.ThemePref;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;

import androidx.appcompat.widget.AppCompatCheckBox;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * List adapter for the list view listing the notes.
 *
 * @author Alkisum
 * @version 2.7
 * @since 1.0
 */
public class NoteListAdapter extends BaseAdapter {

    /**
     * Context.
     */
    private final Context context;

    /**
     * List of notes.
     */
    private final List<Note> notes;

    /**
     * Flag set to true if the edit mode is on, false otherwise.
     */
    private boolean editMode;

    /**
     * NoteListAdapter constructor.
     *
     * @param context Context
     * @param notes   True if the edit mode is on, false otherwise
     */
    public NoteListAdapter(final Context context, final List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    /**
     * Replace the list of notes with the given one.
     *
     * @param notes List of notes
     */
    public final void setNotes(final List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
    }

    /**
     * @return true if the edit mode is on, false otherwise
     */
    public final boolean isEditMode() {
        return editMode;
    }

    /**
     * Set the edit mode and deselect all notes.
     *
     * @param editMode True if the edit mode is on, false otherwise
     */
    public final void setEditMode(final boolean editMode) {
        this.editMode = editMode;
        for (Note note : notes) {
            note.setSelected(false);
        }
    }

    /**
     * Change the selection state of the note stored at the given position in
     * the list.
     *
     * @param position Position of the note in the list
     */
    public final void setNoteSelected(final int position) {
        Note note = notes.get(position);
        note.setSelected(!note.isSelected());
    }

    /**
     * Select all the notes stored in the list.
     */
    public final void selectAll() {
        for (Note note : notes) {
            note.setSelected(true);
        }
    }

    @Override
    public final int getCount() {
        return notes.size();
    }

    @Override
    public final Note getItem(final int position) {
        return notes.get(position);
    }

    @Override
    public final long getItemId(final int position) {
        return notes.get(position).getId();
    }

    @Override
    public final View getView(final int position, final View convertView,
                              final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;
        if (view == null || view.getTag() == null) {
            view = inflater.inflate(R.layout.list_item_note, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            view.setTag(holder);
        }
        final Note note = getItem(position);
        final ViewHolder holder = (ViewHolder) view.getTag();

        holder.title.setText(note.getTitle());
        holder.title.setTextColor(ColorPref.getAccentColor(context));
        holder.content.setText(note.getContent());
        holder.time.setText(String.format("%s %s",
                context.getString(R.string.note_last_update),
                new PrettyTime().format(new Date(note.getUpdatedTime()))));
        holder.checkBox.setChecked(note.isSelected());
        holder.checkBox.setButtonTintList(
                ThemePref.getNoteCheckBoxTint(context));

        if (editMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnClickListener(
                    view1 -> note.setSelected(holder.checkBox.isChecked()));
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        return view;
    }

    /**
     * ViewHolder for note list adapter.
     */
    static class ViewHolder {

        /**
         * Note title.
         */
        @BindView(R.id.note_list_title)
        TextView title;

        /**
         * Note content.
         */
        @BindView(R.id.note_list_content)
        TextView content;

        /**
         * Note time.
         */
        @BindView(R.id.note_list_time)
        TextView time;

        /**
         * Note selected state.
         */
        @BindView(R.id.note_list_checkbox)
        AppCompatCheckBox checkBox;

        /**
         * ViewHolder constructor.
         *
         * @param view View to bind with ButterKnife
         */
        ViewHolder(final View view) {
            ButterKnife.bind(this, view);
        }
    }
}

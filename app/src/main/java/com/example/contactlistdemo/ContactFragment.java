package com.example.contactlistdemo;

import android.annotation.SuppressLint;
import android.app.LoaderManager;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.AdapterView;

import android.widget.Toast;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,AdapterView.OnItemClickListener{

    /*private static final int CONTACT_ID_INDEX = 0;
    private static final int CONTACT_KEY_INDEX = 1;
*/
    @SuppressLint({"InlinedApi", "ObsoleteSdkInt"})
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };

    private final static int[] TO_IDS = {
            android.R.id.text1
    };

    @SuppressLint({"InlinedApi", "ObsoleteSdkInt"})
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME

            };

    @SuppressLint({"InlinedApi", "ObsoleteSdkInt"})
    private static final String SELECTION =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
    private String searchString;
    private String[] selectionArgs = { searchString };

    private RecyclerView recyclerView;
    /*long contactId;
    String contactKey;
    Uri contactUri;
    ContactAdapter contactAdapter;*/
    private SimpleCursorAdapter cursorAdapter;

    ArrayList<String> myList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_fragment, container, false);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.my_recycler_view);
        cursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.contacts_list_item,
                null,
                FROM_COLUMNS, TO_IDS,
                0);

        getLoaderManager().initLoader(0, null, this);

        //read contacts from device
        readPhoneContacts();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        ContactAdapter adapter = new ContactAdapter(getActivity(),myList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        selectionArgs[0] = "%" + searchString + "%";
        return new CursorLoader(getActivity(),ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowID) {

        Toast.makeText(getActivity().getApplicationContext(),"Item is clicked",Toast.LENGTH_LONG).show();

        /*Cursor cursor =adapterView.getAdapter().getCursor();
        cursor.moveToPosition(position);
        contactId = cursor.getLong(CONTACT_ID_INDEX);
        Log.d("Contact fragment","contact id: "+contactId);
        contactKey = cursor.getString(CONTACT_KEY_INDEX);
        Log.d("Contact fragment","contact key: "+contactKey);
        contactUri = ContactsContract.Contacts.getLookupUri(contactId, contactKey);
        Log.d("Contact fragment","contact uri: "+contactUri);*/

    }

    // Read and display android phone contacts in list view.
    private void readPhoneContacts()
    {
        // First empty current phone contacts list data.
        int size = myList.size();
        Log.d("Contact fragment","size: "+size);
        for(int i=0;i<size;i++)
        {
            myList.remove(i);
            i--;
            size = myList.size();
        }

        // Get query phone contacts cursor object.
        Uri readContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Log.d("Contact fragment","readContactUri: "+readContactsUri);
        Cursor cursor = getActivity().getContentResolver().query(readContactsUri, null, null, null, null);
        Log.d("Contact fragment","cursor: "+cursor);

        if(cursor!=null)
        {
            cursor.moveToFirst();

            // Loop in the phone contacts cursor to add each contacts in phoneContactsList.
            do{
                // Get contact display name.
                int displayNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String userDisplayName = cursor.getString(displayNameIndex);
                Log.d("Contact fragment","user Display Name: "+userDisplayName);

                // Get contact phone number.
                int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String phoneNumber = cursor.getString(phoneNumberIndex);
                Log.d("Contact fragment","phone number: "+phoneNumber);

                // Get contact phone type.
                String phoneTypeStr = "Mobile";
                int phoneTypeColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                int phoneTypeInt = cursor.getInt(phoneTypeColumnIndex);
                if(phoneTypeInt== ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                {
                    phoneTypeStr = "Home";
                }else if(phoneTypeInt== ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                {
                    phoneTypeStr = "Mobile";
                }else if(phoneTypeInt== ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                {
                    phoneTypeStr = "Work";
                }
                Log.d("Contact fragment","phone type: "+phoneTypeStr);

                StringBuffer contactStringBuf = new StringBuffer();
                contactStringBuf.append(userDisplayName);
                contactStringBuf.append("\r\n");
                contactStringBuf.append(phoneNumber);
                contactStringBuf.append("\r\n");
                contactStringBuf.append(phoneTypeStr);

                Log.d("Contact fragment","contact string buff: "+contactStringBuf);
                myList.add(contactStringBuf.toString());

            }while(cursor.moveToNext());

            //sorting contacts alphabetically
            Collections.sort(myList);

            // Refresh the listview to display read out phone contacts.
            //contactAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }
}

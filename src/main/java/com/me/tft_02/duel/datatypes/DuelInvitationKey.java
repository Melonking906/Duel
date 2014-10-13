package com.me.tft_02.duel.datatypes;

import com.me.tft_02.duel.util.Misc;

public class DuelInvitationKey
{
    private String playerName;
    private int timestamp;

    public DuelInvitationKey( String playerName )
    {
        this.setPlayerName( playerName );
        this.setTimestamp( Misc.getSystemTime() );
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName( String playerName )
    {
        this.playerName = playerName;
    }

    public int getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( int timestamp )
    {
        this.timestamp = timestamp;
    }
}

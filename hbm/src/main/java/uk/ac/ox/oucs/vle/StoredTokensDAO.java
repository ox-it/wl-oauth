package uk.ac.ox.oucs.vle;

public interface StoredTokensDAO {
	
	void save(StoredTokens tokens);
	
	public StoredTokens loadByToken(String token);
	
	public void removeOldTokens();

}
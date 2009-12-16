package uk.ac.ox.oucs.vle;

import java.util.List;

public interface StoredTokensDAO {
	
	void save(StoredTokens tokens);
	
	public StoredTokens loadByToken(String token);
	
	public List<StoredTokens> loadByUser(String userId);
	
	public void remove(String tokenId);
	
	public void removeOldTokens();

}
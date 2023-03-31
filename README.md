# java-filmorate

![alt text](ER-Diagram-new.png)

### Sample SQL commands:
<pre>
- Get all films -  
    SELECT * FROM film;
    
- Get N-film -  
    SELECT * FROM film WHERE id = N;
    
- Get N of most polular films -  
    SELECT f.id, f.name FROM film AS f  
    LEFT JOIN film_likes AS fl ON f.id=fl.film_id  
    GROUP BY f.id  
    ORDER BY COUNT(fl.user_id) DESC  
    LIMIT(n);
    
- Get all users -  
    SELECT * FROM user;
    
- Get N-user -  
    SELECT * FROM user WHERE id = N;
    
- Get N-user friends -  
    SELECT f.id, f.name FROM user AS u
    LEFT JOIN user AS f ON u.friends=f.id
    WHERE u.id = n;
    
- Get commond friends of A/B users - 
    SELECT id, name FROM user 
    WHERE id IN (
        SELECT f.id FROM user AS u 
        LEFT JOIN user AS f ON u.friends=f.id WHERE u.id = B);
</pre>

SELECT _id, name, description 
FROM roles JOIN set_roles ON _id = id_role
WHERE id_set = id or id_set = (SELECT parent FROM sets s WHERE s._id = id)
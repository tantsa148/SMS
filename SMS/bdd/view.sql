CREATE OR REPLACE VIEW vue_numeros_combine AS
SELECT 
    n.id_numero,
    n.valeur_numero,
    u.id AS id_utilisateur,
    u.username AS utilisateur,
    p.id AS id_plateforme,
    p.nom_plateform AS plateforme
FROM numero n
LEFT JOIN possede ps ON n.id_numero = ps.id_numero
LEFT JOIN users u ON ps.id_utilisateur = u.id
LEFT JOIN disponible_sur ds ON n.id_numero = ds.id_numero
LEFT JOIN plateforme p ON ds.id_plateforme = p.id;

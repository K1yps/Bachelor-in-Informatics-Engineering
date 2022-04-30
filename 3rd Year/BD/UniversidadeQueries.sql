-- Obter os alunos que frequentam uma dada UC, Neste caso Calculo
SELECT Aluno_Número 
FROM aluno_frequenta_uc
INNER JOIN uc ON uc.Código=aluno_frequenta_uc.UC_Código
WHERE uc.Código='Calc_G005'
;

-- Obter os alunos que frequentam o turno 10
SELECT aluno.Número, aluno.Nome
from aluno INNER JOIN
aluno_frequenta_uc ON aluno_frequenta_uc.Aluno_Número = aluno.Número
	where aluno_frequenta_uc.Turno_Número = 10
    ;

-- Procedimento que calcula os alunos pertencentes a um dado curso
DELIMITER $$
CREATE PROCEDURE AlunosDeCurso (Curso varchar(45))
BEGIN
	SELECT aluno FROM aluno
	INNER JOIN matrícula
		ON matrícula.Aluno_Número = aluno.Número
	where  matrícula.Curso_Codigo = Curso
    ;
END $$





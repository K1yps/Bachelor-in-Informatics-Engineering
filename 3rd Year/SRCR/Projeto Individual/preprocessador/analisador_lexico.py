import ply.lex as lex
import re

literals = [';', ':', '(', ')', '-', ',', '/']
tokens = ['FNUM', 'HousesNDirections', 'NUM', 'STRING']


def t_FNUM(t):
    r'[+\-]?\d+[,.]\d+'
    t.value = re.sub(r',', '.', t.value)
    return t


t_HousesNDirections = r'(\(\d*->\d*\))+'
t_NUM = r'[+\-]?\d+'
t_ignore = ' \n\t'
t_STRING = r'[^\W\d](((\w-\w|\w)|[. ])*\w)?'


def t_error(t):
    print("Syntax Error: ", t)
    t.lexer.skip(1)


lexer = lex.lex(reflags=re.UNICODE)

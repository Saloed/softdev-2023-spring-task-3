from sympy import *

compiled = compile("", "Code", 'exec')
def compileCode(toCompile):
    global compiled
    try:
        compiled = compile(toCompile, "Code", 'exec')
        return "OK"
    except Exception as Error:
        return str(Error) + " " + str(Error.args[1][1]) + " " + str(Error.args[1][2])

def main(constants, toEval):
    dloc = {}
    try:
        #local_globals = {k: v for k, v in globals().items()}
        exec(constants, globals(), dloc)
    except Exception as Error:
        return f"Error while compiling constants: {Error}"

    for i in dloc:
       exec(f"{i} = {dloc[i]}", globals())

    try:
        try:
            exec(compiled, globals())
        except Exception as Error:
            return f"Error while running code: {Error}"

        try:
            if toEval == "":
               result = ""
            else:
               result = eval(toEval)
               if type(result) is str: result = "\"" + str(result) + "\""
        except Exception as Error:
            return f"Error while compiling expression: {Error}"

        return str(result)
    except Exception as Error:
        return Error
    finally:

        for i in dloc:
            del globals()[i]

def symPy(func, what):
    try:
        return str(eval(func + '("' + what + '")'))
    except Exception as Error:
        return f"Error while running code: {Error}"

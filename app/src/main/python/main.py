def main(toExec, toEval):
    try:
        try:
            exec(toExec, globals())
        except Exception as Error:
            return f"Error while trying to exec(): {Error}"

        try:
            if toEval == "":
               result = ""
            else:
               result = eval(toEval)
               if type(result) is str: result = "\"" + str(result) + "\""
        except Exception as Error:
            return f"Error while trying to eval(): {Error}"



        return str(result)
    except Exception as Error:
        return Error


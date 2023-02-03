
def main(toExec, toEval):
    try:
        print(f'{toExec = }\n{toEval = }')
        exec(toExec)
        return eval(toEval)
    except Exception as Error:
        return Error
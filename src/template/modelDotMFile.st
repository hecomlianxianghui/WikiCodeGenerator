modelDotMFile(className, dotMHeaderComment, objFields) ::= <<
<dotMHeaderComment>

#import "<className>.h"

@implementation <className>

+ (<className> *)modleFromDic:(NSDictionary *)Dic {
	<className> *obj = [<className> new];
    <objFields; separator="\n">
}

@end
>>
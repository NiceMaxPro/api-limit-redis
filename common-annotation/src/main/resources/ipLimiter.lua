--获取KEY
local ipAddress = KEYS[1]
-- 获取在规定时间内点击的次数
local val = redis.call('incr', ipAddress)
-- 获取剩余时间
local ttl = redis.call('ttl', ipAddress)

-- 获取ARGV内的参数并打印
local expire = ARGV[1]
local count = ARGV[2]

if val == 1 then
    redis.call('expire', ipAddress, tonumber(expire))
else
    if ttl == -1 then
        redis.call('expire', ipAddress, tonumber(expire))
    end
end

if val > tonumber(count) then
    return 0
end
return 1
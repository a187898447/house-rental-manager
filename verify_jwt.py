#!/usr/bin/env python3
import jwt
import json

token = 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoibGFuZGxvcmQiLCJvcGVuaWQiOiIxMzAyNzMwMzg5NyIsInVzZXJJZCI6MSwic3ViIjoiMTMwMjczMDM4OTciLCJpYXQiOjE3NzQ5MjY2MzQsImV4cCI6MTc3NTUzMTQzNH0.QmwYw1RpUlimQ5e8kJjWbyndH1pzKMLVWHR3jEQOtIs'
secret = 'house-rental-manager-jwt-secret-key-2026'
wrong_secret = 'mySecretKeyForRentalApp123456789'

print("Testing JWT token validation...")
print("=" * 50)

try:
    # 尝试用正确的密钥验证
    decoded = jwt.decode(token, secret, algorithms=['HS256'])
    print("✅ Token 验证成功！(使用密钥：house-rental-manager-jwt-secret-key-2026)")
    print("Decoded payload:")
    print(json.dumps(decoded, indent=2))
except jwt.ExpiredSignatureError:
    print("❌ Token 已过期")
except jwt.InvalidTokenError as e:
    print(f"❌ Token 无效：{e}")
    
    # 尝试用错误的密钥验证，看看是否是密钥问题
    print("\n尝试使用旧密钥验证...")
    try:
        decoded_wrong = jwt.decode(token, wrong_secret, algorithms=['HS256'])
        print("⚠️  警告：Token 是用旧密钥签发的！这可能是 401 的原因。")
        print("Decoded with wrong key:")
        print(json.dumps(decoded_wrong, indent=2))
    except jwt.InvalidTokenError:
        print("❌ Token 也不是用旧密钥签发的")
